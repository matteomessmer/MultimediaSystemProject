from os import listdir
from os.path import isfile, join
import re

paths = ['C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing1/test_1_jpg',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing1/test_2_jpg',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing1/test_3_png',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing1/test_4_jpg',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing2/test_1_jpg',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing2/test_2_jpg',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing2/test_3_png',
'C:/Users/matteo/MultimediaSystemProject/output/denoise_on/regionGrowing2/test_4_jpg'
]


p = re.compile('threshold_([0-9]+)_time_([0-9]+)_regions_([0-9]+).jpg')
i = 0
for path in paths:
	csv = "threshold;time;regions;\n"
	onlyvalidfiles = [f for f in listdir(path) if isfile(join(path, f)) and (".jpg" in f  or ".png" in f)]
	for file in onlyvalidfiles:
		m = p.match(file)
		csv = csv + m.group(1) + ";" + m.group(2) + ";" +m.group(3) + ";\n"
	f = open(path + "/data" + str(i) + ".csv", "w")
	f.write(csv)
	f.close()
	i = i + 1