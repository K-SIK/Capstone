import time
import os
import hashlib

from absl import app, flags, logging
from absl.flags import FLAGS
import tensorflow as tf
import lxml.etree
import tqdm

DATA_PATH = 'C:/Users/wjsdu/OneDrive/Desktop/Capstone_largefiles/DeepLearning/YOLOV3/data'
# 데이터 경로
# [이미지] 훈련: Training/TRAIN_IMAGE_0XX/디렉터리/사진 파일, 검증: Validation/VAL_IMAGE_0XX/디렉터리/사진 파일
# [어노테이션] 훈련: Training/TRAIN_LABEL/디렉터리/xml 파일, 검증: Validation/VAL_LABEL/디렉터리/xml 파일
flags.DEFINE_string('data_dir', DATA_PATH + '/images/koreanfood',
                    'path to custom dataset(korean food)')
# # train/val split
# VOC 데이터셋에는 train/val을 여러 종류로 나눌 수 있게 하도록 파일명을 묶어서 저장한 txt 파일이 있는데, 그것을 전달
flags.DEFINE_enum('split', 'Training', ['Training', 'Validation'],
                  'specify train or val split')

# 아웃풋 경로.포맷
# koreanfood_train.tfrecord 또는 koreanfood_val.tfrecord 지정
flags.DEFINE_string('output_file', DATA_PATH + '/koreanfood_train.tfrecord', 
                    'output dataset')
# 라벨 파일 경로.포맷
flags.DEFINE_string('classes', DATA_PATH + '/images/koreanfood/koreanfood.names', 
                    'classes file')


# 매 이미지파일마다 호출
# TFRecord로 저장하기 알맞은 형태로 변환
# <annotation> 태그와 <object> 태그 안에 어떤 태그들이 있는 지 중요!!!
def build_example(annotation, sub_path, class_map):
    ''' 해당 소스 코드는 VOC 데이터셋에 커스텀된 코드임. (수정 필요!!!) '''
    ''' 전달받은 annotation 딕셔너리를 이용해 해당 이미지에 대한 정보들을 이용'''
    # 이미지 파일 경로.파일명 ex) FLAGS.data_dir/JPEGImages/2007_000027.jpg
    img_path = DATA_PATH + '/images/koreanfood/Images' + '/' + sub_path + '/' + annotation['filename']
    '''
    img_path = os.path.join(
        FLAGS.data_dir, 'JPEGImages', annotation['filename'])
    '''
    # 이미지 읽어오기
    img_raw = open(img_path, 'rb').read()
    key = hashlib.sha256(img_raw).hexdigest()

    # 가로/세로 값
    width = int(annotation['size']['width'])
    height = int(annotation['size']['height'])

    # 이미지 파일에서 뽑아낼 정보들
    xmin = []
    ymin = []
    xmax = []
    ymax = []
    classes = []
    classes_text = []
    truncated = []
    views = []
    difficult_obj = []

    if 'object' in annotation: # 객체가 있다면, 
        for obj in annotation['object']: # 'object' 키에 해당하는 값인 리스트 안의 객체들에 대해
            # 각 정보들을 뽑아냄
            difficult = bool(int(obj['difficult']))
            difficult_obj.append(int(difficult))

            xmin.append(float(obj['bndbox']['xmin']) / width)
            ymin.append(float(obj['bndbox']['ymin']) / height)
            xmax.append(float(obj['bndbox']['xmax']) / width)
            ymax.append(float(obj['bndbox']['ymax']) / height)
            classes_text.append(obj['name'].encode('utf8'))
            classes.append(class_map[obj['name']])
            truncated.append(int(obj['truncated']))
            views.append(obj['pose'].encode('utf8'))

    # <annotation>의 각 정보를 변환해서 반환
    example = tf.train.Example(features=tf.train.Features(feature={
        'image/height': tf.train.Feature(int64_list=tf.train.Int64List(value=[height])),
        'image/width': tf.train.Feature(int64_list=tf.train.Int64List(value=[width])),
        'image/filename': tf.train.Feature(bytes_list=tf.train.BytesList(value=[
            annotation['filename'].encode('utf8')])),
        'image/source_id': tf.train.Feature(bytes_list=tf.train.BytesList(value=[
            annotation['filename'].encode('utf8')])),
        'image/key/sha256': tf.train.Feature(bytes_list=tf.train.BytesList(value=[key.encode('utf8')])),
        'image/encoded': tf.train.Feature(bytes_list=tf.train.BytesList(value=[img_raw])),
        'image/format': tf.train.Feature(bytes_list=tf.train.BytesList(value=['jpeg'.encode('utf8')])),
        'image/object/bbox/xmin': tf.train.Feature(float_list=tf.train.FloatList(value=xmin)),
        'image/object/bbox/xmax': tf.train.Feature(float_list=tf.train.FloatList(value=xmax)),
        'image/object/bbox/ymin': tf.train.Feature(float_list=tf.train.FloatList(value=ymin)),
        'image/object/bbox/ymax': tf.train.Feature(float_list=tf.train.FloatList(value=ymax)),
        'image/object/class/text': tf.train.Feature(bytes_list=tf.train.BytesList(value=classes_text)),
        'image/object/class/label': tf.train.Feature(int64_list=tf.train.Int64List(value=classes)),
        'image/object/difficult': tf.train.Feature(int64_list=tf.train.Int64List(value=difficult_obj)),
        'image/object/truncated': tf.train.Feature(int64_list=tf.train.Int64List(value=truncated)),
        'image/object/view': tf.train.Feature(bytes_list=tf.train.BytesList(value=views)),
    }))
    return example


# 객체 탐지에 필요한 태그들(object - class, x, y, etc.)만 파싱
# 재귀 구조
def parse_xml(xml):
    if not len(xml): # 더 이상 자식 태그가 없으면
        return {xml.tag: xml.text} # '태그: 내용' 반환

    # {자식태그1: 내용1, 자식태그2: 내용2. ...}
    result = {} 

    for child in xml: # 각 자식 태그에 대해
        child_result = parse_xml(child) # 재귀 호출 -> {자식태그: {태그1: 내용1, ...}} 반환
        if child.tag != 'object': # 자식 태그가 객체 태그가 아니면, 
            result[child.tag] = child_result[child.tag] # result에 {태그1: 내용1, ...} 추가
        else:                     # 객체 태그이면, (child.tag == 'object')
            '''
            if child.tag not in result: # 처음 나온 object 태그이면
                result[child.tag] = []  # object 키를 가지는 리스트를 먼저 만들고
            result[child.tag].append(child_result[child.tag]) # 태그에 대한 내용을 'object' 리스트에 추가
            '''
            if 'object' not in result:
                result['object'] = []
            result['object'].append(child_result[child.tag])
    
    # {본인 태그: {자식태그1: 내용1, 자식태그2: 내용2. ...}}
    return {xml.tag: result} 


# 어노테이션 파일(xml)을 TFRecord 파일로 변환하여 저장 
def main(_argv):
    # 라벨 데이터 로드
    class_map = {name: idx for idx, name in enumerate(
        open(FLAGS.classes, encoding='UTF8').read().splitlines())}
    logging.info("Class mapping loaded: %s", class_map)
    
    # TFRecord 포맷 writer
    writer = tf.io.TFRecordWriter(FLAGS.output_file)
    '''
    # VOC 데이터셋의 ImageSets/Main/<FLAGS.split>.txt 에 있는 파일명들을 가져옴
    # ImageSets 폴더는 VOC 데이터셋을 train/validation 데이터셋으로 나눌 때 나누기 쉽게 이미지 파일명들을 모아 놓은 txt 파일들이 존재함. 
    image_list = open(os.path.join(
        FLAGS.data_dir, 'ImageSets', 'Main', '%s.txt' % FLAGS.split)).read().splitlines()
    logging.info("Image list loaded: %d", len(image_list))
    '''
    
    # 각 이미지 파일명에 대해 해당 파일명의 어노테이션 파일을 파싱하여 TFRecord로 저장
    # Train/Val 데이터셋을 만들 어노테이션 파일들이 필요함

    # [이미지] Images/Training(Validation)/TRAIN_0XX(VAL_0XX)/분류 디렉터리/*.jpg
    # [라벨] Annotations/Training(Validation)/TRAIN_0XX(VAL_0XX)/분류 디렉터리/*.xml
    # 결국 최초 진입 디렉터리인 Images/Annotations 경로만 다르고 그 아래로는 동일 경로(포맷은 당연히 jpg/xml로 다름)
    ANNOTATION_PATH = DATA_PATH + '/images/koreanfood/Annotations'
    path = ANNOTATION_PATH + '/' + FLAGS.split
    # print(path)
    for main_dir in os.listdir(path):
        path = ANNOTATION_PATH + '/' + FLAGS.split + '/' + main_dir
        for sub_dir in os.listdir(path):
            path = ANNOTATION_PATH + '/' + FLAGS.split + '/' + main_dir + '/' + sub_dir
            print(path)
            for xml_file in os.listdir(path):
                if os.path.splitext(xml_file)[1][1:] != 'xml': continue
                print(os.path.splitext(xml_file)[1][1:])
                # 어노테이션 파일 경로
                annotation_xml = path + '/' + xml_file
                # 어노테이션 파일 읽기
                annotation_xml = lxml.etree.fromstring(open(annotation_xml).read())
                # 읽은 어노테이션 파일을 파싱, 'annotation' key에 해당하는 것만 가져옴
                annotation = parse_xml(annotation_xml)['annotation']
                # tf.Example 객체 생성
                tf_example = build_example(annotation, sub_dir[len(ANNOTATION_PATH)+1:], class_map)
                # TFRecord 파일로 저장
                writer.write(tf_example.SerializeToString())


        

    '''
    for name in tqdm.tqdm(image_list):
        # 어노테이션 파일 경로
        annotation_xml = os.path.join(
            FLAGS.data_dir, 'Annotations', name + '.xml')
        # 어노테이션 파일 읽기
        annotation_xml = lxml.etree.fromstring(open(annotation_xml).read())
        # 읽은 어노테이션 파일을 파싱, 'annotation' key에 해당하는 것만 가져옴
        annotation = parse_xml(annotation_xml)['annotation']
        # tf.train.Example 객체
        tf_example = build_example(annotation, class_map)
        # TFRecord 파일로 저장
        writer.write(tf_example.SerializeToString())
    '''

    writer.close()
    logging.info("Done")


if __name__ == '__main__':
    app.run(main)