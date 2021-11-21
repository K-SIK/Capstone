# *.weights 파일을 *.tf 파일로 변환
# 추후에 저장한 .tf 포맷 가중치를 모델에 로드하여 사용 

from absl import app, flags, logging
from absl.flags import FLAGS
import numpy as np
from yolov3_tf2.models import YoloV3, YoloV3Tiny
from yolov3_tf2.utils import load_darknet_weights
import tensorflow as tf

# 사용할 모델 가중치 경로(*.weights)
flags.DEFINE_string('weights', './data/yolov3.weights', 'path to weights file')
# 가중치를 .tf 포맷으로 저장(*.tf) -> 모델의 전이학습을 위해서는 .tf 포맷을 사용
flags.DEFINE_string('output', './checkpoints/yolov3.tf', 'path to output')
# 타이니 욜로 모드
flags.DEFINE_boolean('tiny', False, 'yolov3 or yolov3-tiny')
# 분류 클래스 수
flags.DEFINE_integer('num_classes', 80, 'number of classes in the model')


def main(_argv):
    physical_devices = tf.config.experimental.list_physical_devices('GPU')
    if len(physical_devices) > 0:
        tf.config.experimental.set_memory_growth(physical_devices[0], True)

    if FLAGS.tiny:
        yolo = YoloV3Tiny(classes=FLAGS.num_classes)
    else:
        yolo = YoloV3(classes=FLAGS.num_classes)
    yolo.summary()
    logging.info('model created')

    load_darknet_weights(yolo, FLAGS.weights) # FLAGS.tiny
    logging.info('weights loaded')

    img = np.random.random((1, 320, 320, 3)).astype(np.float32)
    output = yolo(img)
    logging.info('sanity check passed')

    yolo.save_weights(FLAGS.output)
    logging.info('weights saved')


if __name__ == '__main__':
    try:
        app.run(main)
    except SystemExit:
        pass
