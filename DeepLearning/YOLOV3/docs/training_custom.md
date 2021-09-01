# 학습 과정 - 데이터셋 준비부터 훈련 시작까지

<br>

먼저 학습 데이터셋과 검증 데이터셋을 다운로드 받아서 data 디렉터리 밑에 커스텀 디렉터리를 만들어 안에 저장합니다. 

<br>

다음으로 데이터를 train/validation으로 split하고, annotation되어 있는 xml 파일에서 필요한 부분들만 parsing하여, TFRecord 형식으로 저장합니다. 이 부분은 voc2012.py의 main( )과 parse_xml( ) , build_example( ) 메서드를 참고합니다. 이 메서드들을 조금만 수정하면 커스텀 데이터를 TFRecord 파일로 변환하여 저장할 수 있습니다. 

<br>

TFRecord로 변환을 완료하였으면 학습을 시작합니다. 

학습을 할 때는 다음 옵션들을 전달합니다. (자세한 설명은 train.py 참고)

* `--dataset`: tfrecord 훈련 데이터셋 경로
* `--val`: tfrecord 검증 데이터셋 경로
* `--classes`: 라벨 데이터 경로(*.names)
* `--num_classes`: 분류 클래스 수
* `--mode fit`: 훈련 모드(fit, eager_fit, eager_tf 중 1)
* `--transfer`: 전이 학습 여부 (none, darknet, no_output, frozen, fine_tune 중 1)
* `--size`: 모델의 입력 이미지 크기. 기본으로 416으로 설정되어 있어 전달할 필요 X
* `--epochs`: 에포크 수
* `--batch_size`: 배치 크기
* `--learning_rate`: 학습률(기본으로 0.001으로 설정)
* `--weights_num_classes`: 원하는 출력 크기와 다른 모델로부터 전이 학습을 할 때 사용
* `--multi_gpu`: 2개 이상의 가용 GPU가 있을 때 사용

모든 데이터로 학습을 마치거나, 조기 학습 종료 등으로 학습이 종료되면 checkpoints 디렉터리 아래에 매 에포크마다 저장된 가중치를 확인할 수 있습니다. 



