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


def evaluate(*args):
    input = []
    for inp in args:
        if (inp != None):
            print(inp)
            input.append(inp)
    ######## data
    sampleRate = 16.67
    data_collect = np.empty([1000])  ### sampling rate is 16.67, spinning 60 seconds gives 1000 samples.
    i = 0
    for num in input:
        data_collect[i] = num
        i += 1
    data = data_collect
    #     #################
    #     rsp_y_ori = read_csv_file('./001/001-15.csv')
    #     res_ydem = signal.resample(rsp_y_ori, 2051)
    #     y = res_ydem[0::2]
    #     data = y
    #     ###############
    # print(data.shape)
    ###################
    # BandPass - # Previous results
    wn = 2 * 0.2 / sampleRate
    b, a = signal.butter(8, wn, 'highpass')
    rsp_y = signal.filtfilt(b, a, data)

    wn = 2 * 0.5 / sampleRate
    b, a = signal.butter(8, wn, 'lowpass')
    rsp_y = signal.filtfilt(b, a, rsp_y)
    peaks, _ = find_peaks(rsp_y, height=35, distance=30)
    return len(peaks)