# 폴더 내의 모든 jpeg 확장자를 jpg로 변환하는 프로그램
import os
from absl import flags
from absl.flags import FLAGS
from glob import glob

DATA_PATH = 'C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/Images'

flags.DEFINE_string('path', './', 'directory path to change file formats')

for top_dir in ['Training', 'Validation']:
    path = DATA_PATH + '/' + top_dir
    for main_dir in os.listdir(path):
        path = DATA_PATH + '/' + top_dir + '/' + main_dir
        for sub_dir in os.listdir(path):
            path = DATA_PATH + '/' + top_dir + '/' + main_dir + '/' + sub_dir
            print(path + '/' + '*.jpeg')
            jpeg_files = dict(zip(glob(path + '/' + '*.jpeg'), [1]*10**4))
            print(len(jpeg_files))
            if len(jpeg_files) != 0:
                for file in jpeg_files.keys():
                    try:
                        os.rename(file, os.path.splitext(file)[0] + '.jpg')
                    except:
                        pass
            print("In path: {} JPEG format file: {}\n\n".format(path, len(glob(path + '/' + '*.jpeg'))))


