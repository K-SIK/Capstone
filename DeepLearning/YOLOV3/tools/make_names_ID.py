import os

path = "C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/Annotations/Validation"

names_ID = []
for dir in os.listdir(path):
    names_ID += os.listdir(path + '/' + dir)

f = open("C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/koreanfood_ID.names", 
        'w', encoding='UTF8')
for id in names_ID:
    f.write(id+'\n')
