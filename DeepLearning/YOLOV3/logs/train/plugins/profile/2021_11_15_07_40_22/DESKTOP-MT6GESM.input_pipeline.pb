  *????@Q??wIA2u
>Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map?,{?e@!-??6g?X@)?ެA{d@1?ݫ??{W@:Preprocessing2?
`Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap[0]::TFRecordkH?c?{ @!??o?@)kH?c?{ @1??o?@:Advanced file read2g
0Iterator::Root::Prefetch::ParallelMapV2::BatchV2?f??e@!q????X@)?\?wb?@1X???s??:Preprocessing2^
'Iterator::Root::Prefetch::ParallelMapV2?|$%=??!(?????)?|$%=??1(?????:Preprocessing2?
SIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMapD?ͩd? @!΋????@)}???????1MyF9???:Preprocessing2O
Iterator::Root::Prefetcho?m???!??(?:l??)o?m???1??(?:l??:Preprocessing2p
9Iterator::Root::Prefetch::ParallelMapV2::BatchV2::ShuffleJ??4??e@!?/-?X@)=((E+???1???n;??:Preprocessing2E
Iterator::Root{?2Q?ԝ?!wV????)?w.???1? ?-?w?:Preprocessing2~
GIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap????? @!8?M??@)2t젂?1???x?[u?:Preprocessing:?
]Enqueuing data: you may want to combine small input data chunks into fewer but larger chunks.
?Data preprocessing: you may increase num_parallel_calls in <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#map" target="_blank">Dataset map()</a> or preprocess the data OFFLINE.
?Reading data from files in advance: you may tune parameters in the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch size</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave cycle_length</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer_size</a>)
?Reading data from files on demand: you should read data IN ADVANCE using the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer</a>)
?Other data reading or processing: you may consider using the <a href="https://www.tensorflow.org/programmers_guide/datasets" target="_blank">tf.data API</a> (if you are not using it now)?
:type.googleapis.com/tensorflow.profiler.BottleneckAnalysisk
unknownTNo step time measured. Therefore we cannot tell where the performance bottleneck is.no*noZno#You may skip the rest of this page.BZ
@type.googleapis.com/tensorflow.profiler.GenericStepTimeBreakdown
  " * 2 : B J R Z b JGPUb??No step marker observed and hence the step time is unknown. This may happen if (1) training steps are not instrumented (e.g., if you are not using Keras) or (2) the profiling duration is shorter than the step time. For (1), you need to add step instrumentation; for (2), you may try to profile longer.