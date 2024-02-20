import torch
import torch.nn as nn
import torch.nn.functional as F

class Feature(nn.Module):
    def __init__(self):
        super(Feature, self).__init__()

        self.fc1 = nn.Linear(1000, 256)
        self.fc2 = nn.Linear(256, 64)
        self.fc3 = nn.Linear(64, 5)

        self.RLU = nn.LeakyReLU()

    def forward(self, x):
        x = self.RLU(self.fc1(x))
        x = self.RLU(self.fc2(x))
        x = self.fc3(x)
        return x