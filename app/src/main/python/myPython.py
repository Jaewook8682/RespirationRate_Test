import numpy as np
import os
import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import torchvision.models as  models
import matplotlib.pyplot as plt
import model as md
from torchvision.models import resnet18


def test_call():
    print("This is python code in android studio!")
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    model_path = os.path.join(os.path.dirname(__file__), 'dataset', 'model_state_dict.pth')
    data_path  =  os.path.join(os.path.dirname(__file__), 'dataset', 'test_x.npy')
    label_path =  os.path.join(os.path.dirname(__file__), 'dataset', 'test_y.npy')

    test_x = np.load(data_path, allow_pickle=True)
    test_y = np.load(label_path, allow_pickle=True)
    print(np.shape(test_x))
    print(np.shape(test_y))
    test_x = test_x[100:120].tolist()
    test_y = test_y[100:120].tolist()

    a = torch.Tensor(test_x).to(device)
    b = torch.Tensor(test_y).to(device)

    model = md.Feature()
    model.load_state_dict(torch.load(model_path))
    model = model.to(device)


    y_pred = model(a)
    print(y_pred)
    out = torch.argmax(y_pred, dim=1)
    print("Out : ", out)
    print("Y : ", b)
    acc = (out == b).float().mean()
    print("ACC : ", acc)
    return acc











