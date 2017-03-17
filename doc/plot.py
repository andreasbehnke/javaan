#!/usr/bin/python
import sys
import matplotlib.pyplot as plt
import csv

csvFileName = sys.argv[1]

count = []
time = []
timeDivCount = []

with open(csvFileName,'r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        count.append(int(row[0]))
        time.append(int(row[1]))
        timeDivCount.append(int(row[2]))

plt.plot(count,time,'r.', label='time')
plt.plot(count,timeDivCount,'g.', label='time/count')
plt.xlabel('count')
plt.legend()
plt.show()
