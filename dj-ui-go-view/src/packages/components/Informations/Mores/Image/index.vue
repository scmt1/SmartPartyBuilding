<template>
  <div :style="getStyle(borderRadius)">
    <n-image
      :object-fit="fit"
      preview-disabled
      :src="option.dataset"
      :fallback-src="requireErrorImg()"
      :width="w"
      :height="h"
      :style="getRotateStyle(isRotate)"
    ></n-image>
  </div>
</template>

<script setup lang="ts">
import { PropType, shallowReactive, watch, toRefs } from 'vue'
import { requireErrorImg } from '@/utils'
import { useChartDataFetch } from '@/hooks'
import { CreateComponentType } from '@/packages/index.d'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'

const props = defineProps({
  chartConfig: {
    type: Object as PropType<CreateComponentType>,
    required: true
  }
})

const { w, h } = toRefs(props.chartConfig.attr)
const { dataset, fit, borderRadius, isRotate } = toRefs(props.chartConfig.option)

const option = shallowReactive({
  dataset: ''
})

const getStyle = (radius: number) => {
  return {
    borderRadius: `${radius}px`,
    overflow: 'hidden'
  }
}
const getRotateStyle = (rotate: boolean) => {
  if(rotate){
    return {
      animation: 'antRotate 15s infinite linear',
      transform: 'rotate(45deg)'
    }
  }
}

// 编辑更新
watch(
  () => props.chartConfig.option.dataset,
  (newData: any) => {
    option.dataset = newData
  },
  {
    immediate: true
  }
)

// 预览更新
useChartDataFetch(props.chartConfig, useChartEditStore, (newData: any) => {
  option.dataset = newData
})
</script>
