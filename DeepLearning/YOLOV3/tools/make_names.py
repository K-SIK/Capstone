fr = open("C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/_koreanfood.names",
         'r', encoding='UTF8')
fw = open("C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/koreanfood.names",
         'w', encoding='UTF8')

while True:
    line = fr.readline()
    if not line: break
    line = line.split()
    print(line)
    fw.write(line[-2]+'\n')

fr.close()
fw.close()
