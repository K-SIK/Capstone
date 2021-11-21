from struct import unpack
import os
from tqdm import tqdm


marker_mapping = {
    0xffd8: "Start of Image",
    0xffe0: "Application Default Header",
    0xffdb: "Quantization Table",
    0xffc0: "Start of Frame",
    0xffc4: "Define Huffman Table",
    0xffda: "Start of Scan",
    0xffd9: "End of Image"
}


class JPEG:
    def __init__(self, image_file):
        with open(image_file, 'rb') as f:
            self.img_data = f.read()
    
    def decode(self):
        data = self.img_data
        while(True):
            marker, = unpack(">H", data[0:2])
            # print(marker_mapping.get(marker))
            if marker == 0xffd8:
                data = data[2:]
            elif marker == 0xffd9:
                return
            elif marker == 0xffda:
                data = data[-2:]
            else:
                lenchunk, = unpack(">H", data[2:4])
                data = data[2+lenchunk:]            
            if len(data)==0:
                raise TypeError("issue reading jpeg file")      


bads = []

DATA_PATH = 'E:/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/Images'

for top_dir in tqdm(['Training', 'Validation']):
    path = DATA_PATH + '/' + top_dir
    for main_dir in tqdm(os.listdir(path)):
        path = DATA_PATH + '/' + top_dir + '/' + main_dir
        for sub_dir in tqdm(os.listdir(path)):
            path = DATA_PATH + '/' + top_dir + '/' + main_dir + '/' + sub_dir
            for img in os.listdir(path):
                image = os.path.join(path,img)
                image = JPEG(image) 
                try:
                    image.decode()   
                except:
                    bads.append(str(os.path.join(path,img)))
            print(path)

print(*bads, len(bads), sep='n')

for bad_img in bads:
  os.remove(bad_img)