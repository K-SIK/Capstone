  *33333?@䥛?$??@2u
>Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map?C5%Y?P@!]?>?̍U@)?????P@1????K?T@:Preprocessing2g
0Iterator::Root::Prefetch::ParallelMapV2::BatchV2c??*3?S@!ŋ????X@)?je?/U%@1S?^?K+@:Preprocessing2?
`Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap[0]::TFRecord?H.?!}@!???e5?@)?H.?!}@1???e5?@:Advanced file read2p
9Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle
?2??P@!?ԧ-?U@)?q75а?1,?&Yb???:Preprocessing2?
SIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap'????@!??o	@)???m???18=G{???:Preprocessing2~
GIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMapx??Dg@!dy??)p@)>?h??i??1_??H??:Preprocessing2^
'Iterator::Root::Prefetch::ParallelMapV2?ei??r??!???t????)?ei??r??1???t????:Preprocessing2O
Iterator::Root::Prefetch???[???!:#g?T??)???[???1:#g?T??:Preprocessing2E
Iterator::Root]?6??n??!?k2???)??߼8???1?Zp\n??:Preprocessing:?
]Enqueuing data: you may want to combine small input data chunks into fewer but larger chunks.
?Data preprocessing: you may increase num_parallel_calls in <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#map" target="_blank">Dataset map()</a> or preprocess the data OFFLINE.
?Reading data from files in advance: you may tune parameters in the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch size</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave cycle_length</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer_size</a>)
?Reading data from files on demand: you should read data IN ADVANCE using the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer</a>)
?Other data reading or processing: you may consider using the <a href="https://www.tensorflow.org/programmers_guide/datasets" target="_blank">tf.data API</a> (if you are not using it now)?
:type.googleapis.com/tensorflow.profiler.BottleneckAnalysisk
unknownTNo step time measured. Therefore we cannot tell where the performance bottleneck is.no*noZno#You may skip the rest of this page.BZ
@type.googleapis.com/tensorflow.profiler.GenericStepTimeBreakdown
  " * 2 : B J R Z b JGPUb??No step marker observed and hence the step time is unknown. This may happen if (1) training steps are not instrumented (e.g., if you are not using Keras) or (2) the profiling duration is shorter than the step time. For (1), you need to add step instrumentation; for (2), you may try to profile longer.