<template>
  <div
    class="chart-item"
    v-for="(item, index) in componentList"
    :class="animationsClass(item.styles.animations)"
    :key="item.id"
    :style="{
      ...getComponentAttrStyle(item.attr, index),
      ...getFilterStyle(item.styles),
      ...getTransformStyle(item.styles),
      ...getStatusStyle(item.status),
      ...getPreviewConfigStyle(item.preview),
      ...getBlendModeStyle(item.styles) as any
    }"
  >
    <!-- 分组 -->
    <preview-render-group
      v-if="item.isGroup"
      :groupData="(item as CreateComponentGroupType)"
      :groupIndex="index"
      :themeSetting="themeSetting"
      :themeColor="themeColor"
    ></preview-render-group>

    <!-- 单组件 -->
    <component
      v-else
      :is="item.chartConfig.chartKey"
      :id="item.id"
      :chartConfig="item"
      :themeSetting="themeSetting"
      :themeColor="themeColor"
      :style="{ ...getSizeStyle(item.attr) }"
      v-on="useLifeHandler(item)"
    ></component>
  </div>
</template>

<script setup lang="ts">
import { PropType, computed, onMounted, onUnmounted, ref } from 'vue'
import { useChartDataPondFetch } from '@/hooks'
import { ChartEditStorageType } from '../../index.d'
import { PreviewRenderGroup } from '../PreviewRenderGroup/index'
import { CreateComponentGroupType } from '@/packages/index.d'
import { chartColors } from '@/settings/chartThemes/index'
import { useChartEditStore } from '@/store/modules/chartEditStore/chartEditStore'
import { animationsClass, getFilterStyle, getTransformStyle, getBlendModeStyle, colorCustomMerge } from '@/utils'
import { getSizeStyle, getComponentAttrStyle, getStatusStyle, getPreviewConfigStyle } from '../../utils'
import { useLifeHandler } from '@/hooks'

// 初始化数据池
const { initDataPond, clearMittDataPondMap } = useChartDataPondFetch()
const chartEditStore = useChartEditStore()

const props = defineProps({
  localStorageInfo: {
    type: Object as PropType<ChartEditStorageType>,
    required: true
  }
})

// 主题色
const themeSetting = computed(() => {
  const chartThemeSetting = props.localStorageInfo.editCanvasConfig.chartThemeSetting
  return chartThemeSetting
})


// 配置项
const themeColor = computed(() => {
  const colorCustomMergeData = colorCustomMerge(props.localStorageInfo.editCanvasConfig.chartCustomThemeColorInfo)
  return colorCustomMergeData[props.localStorageInfo.editCanvasConfig.chartThemeColor]
})
// 20230328 组件弹窗和切换功能
let componentList = ref(props.localStorageInfo.componentList)
const chartSwitchData=((groupList:any)=>{
  componentList.value=groupList.value
  //操作数据后更新视图
  // internalInstance.proxy.$forceUpdate();
})
import {messageEventListener} from '@/components/OpenLayer/openLayer'
let {groupList}=messageEventListener(chartSwitchData)
groupList.value=componentList.value as []

// end
// 组件渲染结束初始化数据池
clearMittDataPondMap()
onMounted(() => {
  initDataPond(props.localStorageInfo.requestGlobalConfig)
})
</script>

<style lang="scss" scoped>
.chart-item {
  position: absolute;
}
</style>
