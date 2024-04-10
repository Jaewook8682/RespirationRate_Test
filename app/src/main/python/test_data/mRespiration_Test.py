import numpy as np
import csv
from scipy import signal
from scipy.signal import find_peaks


def read_csv_file(filename):
    with open(filename) as csvfile:
        spamreader = csv.reader(csvfile)
        input_data = np.empty([1600])
        for num, row in enumerate(spamreader):

            if num > 19:
                index = num - 20
                input_data[index] = row[3]
        return input_data


def evaluate():
    data_path  =  os.path.join(os.path.dirname(__file__), 'dataset', 'test_x.npy')
    label_path =  os.path.join(os.path.dirname(__file__), 'dataset', 'test_y.npy')
    test_x = np.load(data_path, allow_pickle=True)
    test_y = np.load(label_path, allow_pickle=True)


    data_collect = np.empty([1000])  ### sampling rate is 16.67, spinning 60 seconds gives 1000 samples.
    i = 0
    for num in input:
        data_collect[i] = num
        i += 1
    data = data_collect
    # BandPass - # Previous results
    sampleRate = 16.67
    wn = 2 * 0.2 / sampleRate
    b, a = signal.butter(8, wn, 'highpass')
    rsp_y = signal.filtfilt(b, a, data)

    wn = 2 * 0.5 / sampleRate
    b, a = signal.butter(8, wn, 'lowpass')
    rsp_y = signal.filtfilt(b, a, rsp_y)
    peaks, _ = find_peaks(rsp_y, height=35, distance=30)
    return len(peaks)