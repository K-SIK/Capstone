import time
from absl import app, flags, logging
from absl.flags import FLAGS
import cv2
import numpy as np
import tensorflow as tf

# from ..yolov3_tf2 import models, dataset, utils

from yolov3_tf2.models import (
    YoloV3, YoloV3Tiny
)
from yolov3_tf2.dataset import load_tfrecord_dataset, transform_images
from yolov3_tf2.utils import draw_outputs

# 라벨 데이터 경로
flags.DEFINE_string('classes', './data/coco.names', 'path to classes file')
# 이미지 리사이즈 사이즈
flags.DEFINE_integer('size', 416, 'resize images to')
# 데이터셋 경로
flags.DEFINE_string(
    'dataset', './data/voc2012_train.tfrecord', 'path to dataset')
# 출력 데이터 경로
flags.DEFINE_string('output', './output.jpg', 'path to output image')

# TFRecord에서 로드된 이미지 데이터들 중 하나의 랜덤한 이미지를 출력
def main(_argv):
    class_names = [c.strip() for c in open(FLAGS.classes, encoding='UTF8').readlines()]
    logging.info('classes loaded')

    dataset = load_tfrecord_dataset(FLAGS.dataset, FLAGS.classes, FLAGS.size)
    dataset = dataset.shuffle(512)

    for image, labels in dataset.take(1):
        boxes = []
        scores = []
        classes = []
        for x1, y1, x2, y2, label in labels:
            if x1 == 0 and x2 == 0:
                continue

            boxes.append((x1, y1, x2, y2))
            scores.append(1)
            classes.append(label)
        nums = [len(boxes)]
        boxes = [boxes]
        scores = [scores]
        classes = [classes]

        logging.info('labels:')
        for i in range(nums[0]):
            logging.info('\t{}: {}, {}, {}'.format(int(classes[0][i]), class_names[int(classes[0][i])],
                                               np.array(scores[0][i]),
                                               np.array(boxes[0][i])))

        img = cv2.cvtColor(image.numpy(), cv2.COLOR_RGB2BGR)
        img = draw_outputs(img, (boxes, scores, classes, nums), class_names)
        cv2.imwrite(FLAGS.output, img)
        logging.info('output saved to: {}'.format(FLAGS.output))


if __name__ == '__main__':
    app.run(main)
