<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="饼图配置" :expanded="true">
    <SettingItemBox name="开关">
      <SettingItem>
        <n-space>
          <n-switch v-model:value="optionData.series[0].label.show" size="small"></n-switch>
          <n-text>展示标签</n-text>
        </n-space>
      </SettingItem>

      <setting-item>
        <n-space>
          <n-switch v-model:value="optionData.series[0].labelLine.show" size="small"></n-switch>
          <n-text>引导线</n-text>
        </n-space>
      </setting-item>
    </SettingItemBox>
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

    <setting-item-box name="引导线">
      <setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.series[0].labelLine.lineStyle.width"
              size="small"
              :min="2"
          ></n-input-number>
          <n-text>引导线宽度</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.series[0].labelLine.length"
              size="small"
              :min="2"
          ></n-input-number>
          <n-text>引导线1长度</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.series[0].labelLine.length2"
              size="small"
              :min="2"
          ></n-input-number>
          <n-text>引导线2长度</n-text>
        </n-space>
      </setting-item>
    </setting-item-box>
<!--    <setting-item-box name="圆心标题">
      <setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.textStyle.fontSize"
              size="small"
              :min="0"
          ></n-input-number>
          <n-text>字体大小</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.textStyle.color"></n-color-picker>
          <n-text>字体颜色</n-text>
        </n-space>
      </setting-item>
    </setting-item-box>
    <setting-item-box name="圆心副标题">
      <setting-item>
        <n-space>
          <n-input-number
              v-model:value="optionData.subtextStyle.fontSize"
              size="small"
              :min="0"
          ></n-input-number>
          <n-text>字体大小</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.subtextStyle.color"></n-color-picker>
          <n-text>字体颜色</n-text>
        </n-space>
      </setting-item>
    </setting-item-box>-->
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
