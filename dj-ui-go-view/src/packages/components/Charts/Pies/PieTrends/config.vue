<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem  name="柱状图" :expanded="true">
    <SettingItemBox name="标签">
      <setting-item>
        <setting-item name="颜色">
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.series[0].label.color"></n-color-picker>
        </setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.series[0].label.fontSize"
              size="small"
              :min="2"
          ></n-input-number>
          <n-text>标签字体大小</n-text>
        </n-space>
      </setting-item>
      <SettingItem>
        <n-select v-model:value="optionData.series[0].label.fontFamily" size="small" :options="fontFamilyOptions" />
        <n-text>标签字体系列</n-text>
      </SettingItem>

      <setting-item>
        <n-space>
          <n-select v-model:value="optionData.series[0].label.fontWeight" size="small" :options="fontWeightOptions" />
          <n-text>标签字体粗细</n-text>
        </n-space>
      </setting-item>
    </SettingItemBox>

  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, watch } from 'vue'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
// import { PieTypeObject, PieTypeEnum } from './config'
import { labelConfig } from '@/packages/chartConfiguration/echarts'

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})


const labelFormatterOptions = [
  { label: '数据名', value: '{b}' },
  { label: '百分比', value: '{d}' },
  { label: '列名:百分比', value: '{b}:{d}%' }
]

const fontWeightOptions = [
  {label: ' 细 ', value: 'lighter'},
  {label: ' 中 ', value: 'normal'},
  {label: ' 粗 ', value: 'bolder'}
]

const fontFamilyOptions = [
  {label: 'sans-serif', value: 'sans-serif'},
  {label: 'serif', value: 'serif'},
  {label: 'monospace', value: 'monospace'},
  {label: 'Arial', value: 'Arial'},
  {label: 'Courier New', value: 'Courier New'}
]
</script>
