  *???S???@?$?@A2u
>Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map?o????t@!G?頬X@)ZEhf?s@1?:?ohFW@:Preprocessing2?
`Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap[0]::TFRecord????gG7@!??l?}@)????gG7@1??l?}@:Advanced file read2?
SIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap{j??UM7@!?'?4Є@)?6ǹM???1d>?q#|?:Preprocessing2p
9Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shufflee???t@!@r?J??X@)?a?????1?e??[v?:Preprocessing2O
Iterator::Root::Prefetch#??E????!F?f??p?)#??E????1F?f??p?:Preprocessing2E
Iterator::RootW?c#???!??C{?X??)??? ????1??? ??o?:Preprocessing2~
GIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap?@??_P7@!?s??f?@)5??6?N??1??a?ִl?:Preprocessing2^
'Iterator::Root::Prefetch::ParallelMapV2^?/?ۆ?!"?x??j?)^?/?ۆ?1"?x??j?:Preprocessing:?
]Enqueuing data: you may want to combine small input data chunks into fewer but larger chunks.
?Data preprocessing: you may increase num_parallel_calls in <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#map" target="_blank">Dataset map()</a> or preprocess the data OFFLINE.
?Reading data from files in advance: you may tune parameters in the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch size</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave cycle_length</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer_size</a>)
?Reading data from files on demand: you should read data IN ADVANCE using the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer</a>)
?Other data reading or processing: you may consider using the <a href="https://www.tensorflow.org/programmers_guide/datasets" target="_blank">tf.data API</a> (if you are not using it now)?
:type.googleapis.com/tensorflow.profiler.BottleneckAnalysisk
unknownTNo step time measured. Therefore we cannot tell where the performance bottleneck is.no*noZno#You may skip the rest of this page.BZ
@type.googleapis.com/tensorflow.profiler.GenericStepTimeBreakdown
  " * 2 : B J R Z b JGPUb??No step marker observed and hence the step time is unknown. This may happen if (1) training steps are not instrumented (e.g., if you are not using Keras) or (2) the profiling duration is shorter than the step time. For (1), you need to add step instrumentation; for (2), you may try to profile longer.