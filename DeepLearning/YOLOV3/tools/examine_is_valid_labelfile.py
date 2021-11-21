# 라벨 파일 내에 동일한 클래스명이 있는지 검사
def is_valid_labelfile(filepath):
    label_dict = dict()
    with open(filepath,'r', encoding='UTF8') as f:
        while True:
            label = f.readline().rstrip()
            if not label: break
            label_dict[label] = label_dict.get(label,0) + 1

    ret = ''
    for label in label_dict:
        if label_dict[label] >= 2:
            ret += '{}: {}\n'.format(label, label_dict[label])

    return 'valid' if len(ret) == 0 else ret



filepath = 'E:/Capstone_largefiles/DeepLearning/YOLOV3/data/images/koreanfood/koreanfood.names'
print(is_valid_labelfile(filepath))