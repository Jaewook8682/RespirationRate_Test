import numpy as np
import csv
from scipy import signal
from scipy.signal import find_peaks
import csv
import numpy as np
import os
from scipy import signal
from os.path import dirname
from matplotlib import pyplot as plt



def read_csv_file(filename):
    with open(filename) as csvfile:
        spamreader = csv.reader(csvfile)
        input_data = np.empty([1600])
        for num, row in enumerate(spamreader):

            if num > 19:
                index = num - 20
                input_data[index] = row[3]
        return input_data

def downsampling(data):
    down_sampled = np.empty([0, 500])
    for i in range(1000):
        print(i)
        down_sampled = np.append(down_sampled, [data[i, 0:len(data[0]):2]], axis=0)
    return down_sampled

def evaluate():
    data_path = os.path.join(os.path.dirname(__file__), 'dataset', 'data_0226.npy')
    data = np.load(data_path, allow_pickle=True)
    #data_collect = np.empty([1000])  ### sampling rate is 16.67, spinning 60 seconds gives 1000 samples.
    i = 0

    data1 = data[:, 0:len(data[0]):2]
    data2 = signal.resample(data1, 1000, axis=1)
    # BandPass - # Previous results
    sampleRate = 16.67
    wn = 2 * 0.2 / sampleRate
    b, a = signal.butter(8, wn, 'highpass')
    rsp_y = signal.filtfilt(b, a, data2)
    wn = 2 * 0.5 / sampleRate
    b, a = signal.butter(8, wn, 'lowpass')
    rsp_y = signal.filtfilt(b, a, rsp_y)
    peaks, _ = find_peaks(rsp_y[0], height=35, distance=30)    #pick index since function "find_peaks" accept 1D value
    return len(peaks)