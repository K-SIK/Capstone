?  *??????@????|B?@2u
>Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map??QI?xL@!?g??dkU@))?Ǻ8K@1????zT@:Preprocessing2g
0Iterator::Root::Prefetch::ParallelMapV2::BatchV2???ZӔP@!k?'??X@)??#???"@12?i?+,@:Preprocessing2?
`Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap[0]::TFRecord?sF???@!?????a@)?sF???@1?????a@:Advanced file read2^
'Iterator::Root::Prefetch::ParallelMapV2B>?٬???!<?=???)B>?٬???1<?=???:Preprocessing2p
9Iterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle?&?L@!????pU@))\???(??1x??w/??:Preprocessing2?
SIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMap[0]::FlatMap???9#?@!j?վ?@)	?^)ˠ?1F-???D??:Preprocessing2~
GIterator::Root::Prefetch::ParallelMapV2::BatchV2::Shuffle::Map::FlatMapx$(?@!lv@)?
F%u??1??M?O???:Preprocessing2O
Iterator::Root::PrefetchM??St$??!u??i??)M??St$??1u??i??:Preprocessing2E
Iterator::RootA??ǘ???!?El?7??)46<?R??1I??Tː?:Preprocessing:?
]Enqueuing data: you may want to combine small input data chunks into fewer but larger chunks.
?Data preprocessing: you may increase num_parallel_calls in <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#map" target="_blank">Dataset map()</a> or preprocess the data OFFLINE.
?Reading data from files in advance: you may tune parameters in the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch size</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave cycle_length</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer_size</a>)
?Reading data from files on demand: you should read data IN ADVANCE using the following tf.data API (<a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#prefetch" target="_blank">prefetch</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/Dataset#interleave" target="_blank">interleave</a>, <a href="https://www.tensorflow.org/api_docs/python/tf/data/TFRecordDataset#class_tfrecorddataset" target="_blank">reader buffer</a>)
?Other data reading or processing: you may consider using the <a href="https://www.tensorflow.org/programmers_guide/datasets" target="_blank">tf.data API</a> (if you are not using it now)?
:type.googleapis.com/tensorflow.profiler.BottleneckAnalysisk
unknownTNo step time measured. Therefore we cannot tell where the performance bottleneck is.no*noZno#You may skip the rest of this page.BZ
@type.googleapis.com/tensorflow.profiler.GenericStepTimeBreakdown
  " * 2 : B J R Z b JGPUb??No step marker observed and hence the step time is unknown. This may happen if (1) training steps are not instrumented (e.g., if you are not using Keras) or (2) the profiling duration is shorter than the step time. For (1), you need to add step instrumentation; for (2), you may try to profile longer.Y      Y@qkC???5??"?
unknownTNo step time measured. Therefore we cannot tell where the performance bottleneck is.b
`input_pipeline_analyzer (especially Section 3 for the breakdown of input operations on the Host)Q
Otf_data_bottleneck_analysis (find the bottleneck in the tf.data input pipeline)m
ktrace_viewer (look at the activities on the timeline of each Host Thread near the bottom of the trace view)"O
Mtensorflow_stats (identify the time-consuming operations executed on the GPU)"U
Strace_viewer (look at the activities on the timeline of each GPU in the trace view)*?
?<a href="https://www.tensorflow.org/guide/data_performance_analysis" target="_blank">Analyze tf.data performance with the TF Profiler</a>*y
w<a href="https://www.tensorflow.org/guide/data_performance" target="_blank">Better performance with the tf.data API</a>2M
=type.googleapis.com/tensorflow.profiler.GenericRecommendation
nono2no:
Refer to the TF2 Profiler FAQ2"Nvidia GPU (Turing)(: B??No step marker observed and hence the step time is unknown. This may happen if (1) training steps are not instrumented (e.g., if you are not using Keras) or (2) the profiling duration is shorter than the step time. For (1), you need to add step instrumentation; for (2), you may try to profile longer.JDESKTOP-MT6GESM: Failed to load libcupti (is it installed and accessible?)