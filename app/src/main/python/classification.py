import csv
import numpy as np
import librosa
import torch
from torch.utils.data import Dataset, DataLoader
from torchvision.models import resnet34
from torch.utils.data import TensorDataset
import os
from scipy import signal
from os.path import dirname


class SpecData(Dataset):
    """
    transfer the input data to Spectrogram
    input_data : numpy array [1, 1000]
    """

    def __init__(self, input_data, n_fft=128):
        data = input_data[:, :1000]
        self.data = []
        for i, c in enumerate(data):
            # normalize
            c -= c.min()
            c = ((c / (c.max() + 1e-6)) - 0.5)
            wav = c
            X = librosa.stft(wav, n_fft=n_fft)
            Xdb = librosa.amplitude_to_db(abs(X))

            # Remove cuda as android does not work with cuda
            #            out = torch.from_numpy(spec_to_image(Xdb)).cuda()
            out = torch.from_numpy(spec_to_image(Xdb))
            self.data.append(out[np.newaxis, ...])

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        return self.data[idx]
def preprocessing(input_data):
    data = input_data[:, :1000]
    n_fft = 128
    new_data = []
    for i, c in enumerate(data):
        # normalize
        c -= c.min()
        c = ((c / (c.max() + 1e-6)) - 0.5)
        wav = c
        X = librosa.stft(wav, n_fft=n_fft)
        Xdb = librosa.amplitude_to_db(abs(X))

        # Remove cuda as android does not work with cuda
        #            out = torch.from_numpy(spec_to_image(Xdb)).cuda()
        out = torch.from_numpy(spec_to_image(Xdb))
        new_data.append(out[np.newaxis, ...])
    return new_data

def spec_to_image(spec, eps=1e-6):
    """
    transfer the Spectrogram to Image
    spec : numpy array
    """
    mean = spec.mean()
    std = spec.std()
    spec_norm = (spec - mean) / (std + eps)
    spec_min, spec_max = spec_norm.min(), spec_norm.max()
    spec_scaled = 255 * (spec_norm - spec_min) / (spec_max - spec_min)
    spec_scaled = spec_scaled.astype(np.uint8)
    return spec_scaled


def read_csv_file(filename):
    """
    read csv file.
    filename : path of the file
    """
    with open(filename) as csvfile:
        spamreader = csv.reader(csvfile)
        input_data = np.empty([1, 1000])
        for num, row in enumerate(spamreader):
            if num > 19:
                index = num - 20
                input_data[0, index] = row[3]
        return input_data

def get_multiple(indices):
    classes = ['coughing', 'laughing', 'throat_cleaning', 'speaking', 'walking']
    return[classes[i] for i in indices]

def downsampling(data):
    down_sampled = np.empty([0, 500])
    for i in range(1000):
        down_sampled = np.append(down_sampled, [data[i, 0:len(data[0]):2]], axis=0)
    return down_sampled

def evaluate(*args):
    ###### important: need to specify the path for loading the model correctly
    model_path = os.path.join(dirname(__file__), 'test_data', 'best.pth')
    data_path  = os.path.join(dirname(__file__), 'dataset', 'data_0226.npy')
    label_path  = os.path.join(dirname(__file__), 'dataset', 'label_0226.npy')
    ###################################################################
    print("*************************************************")
    data = np.load(data_path, allow_pickle=True)
    print(np.shape(data))
    data1 = downsampling(data)
    print(np.shape(data1))
    data2 = signal.resample(data1, 1000, axis=1)  ###### resampling the data to obtain 1000 samples.
    print(np.shape(data2))
    label = np.load(label_path, allow_pickle=True)
    print(np.shape(label))
    data_input = preprocessing(data2)
    print(data_input[0].size())
    label = torch.tensor(label.astype(np.float32))

    # Transfer data to spectrogram
    #test_loader = DataLoader(dataset, batch_size=2, shuffle=True)

    # CPU or GPU Mode
    if torch.cuda.is_available():
        device = torch.device('cuda:0')
    else:
        device = torch.device('cpu')

    # Network Loading

    model = resnet34(pretrained=True)
    model.fc = torch.nn.Linear(512, 5)
    model.conv1 = torch.nn.Conv2d(1, 64, kernel_size=(7, 7), stride=(2, 2), padding=(3, 3), bias=False)
    loaded_dict_enc = torch.load(model_path, map_location=torch.device('cpu'))
    model.load_state_dict(loaded_dict_enc)
    model = model.to(device)
    model.eval()
    print("11")
    x = data_input[0]
    print("22")
    x = x.type(torch.float32).to(device)
    print("33")
    y_hat = model(x)
    print(y_hat)
    pred = y_hat.topk(max((1,)), 1, True, True)
    print("==================================")
    print(pred)
    print("--------------------------------")
    result = get_multiple(pred[1].squeeze())
    print(result)
    return 1

