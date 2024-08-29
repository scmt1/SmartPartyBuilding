<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="柱状图" :expanded="true">
    <SettingItemBox name="柱子宽度">
      <n-input-number v-model:value="seriesList[0].symbolSize[0]" size="small" :min="10"></n-input-number>
    </SettingItemBox>
    <SettingItemBox name="柱顶圆偏移">
      <SettingItem name="柱子1">
        <n-slider
                v-model:value="seriesList[8].symbolOffset[0]"
                :min="-100"
                :max="100"
        ></n-slider>
      </SettingItem>
      <SettingItem name="柱子2">
        <n-slider
                v-model:value="seriesList[9].symbolOffset[0]"
                :min="-100"
                :max="100"
        ></n-slider>
      </SettingItem>

    </SettingItemBox>
    <SettingItemBox name="柱子1颜色">
      <setting-item name="顶部颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="文字颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].label.color"></n-color-picker>
      </setting-item>
      <setting-item name="中间上面颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].itemStyle.color.colorStops[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间下面颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].itemStyle.color.colorStops[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="浅色颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[4].itemStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="底座颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[6].color"></n-color-picker>
      </setting-item>
      <setting-item name="封顶颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[8].itemStyle.normal.color"></n-color-picker>
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="柱子2颜色">
      <setting-item name="顶部颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="文字颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].label.color"></n-color-picker>
      </setting-item>
      <setting-item name="中间上面颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].itemStyle.color.colorStops[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间下面颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].itemStyle.color.colorStops[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="浅色颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[5].itemStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="底座颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[7].color"></n-color-picker>
      </setting-item>
      <setting-item name="封顶颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[9].itemStyle.normal.color"></n-color-picker>
      </setting-item>
    </SettingItemBox>
  </CollapseItem>
</template>

<script setup lang="ts">
  import {PropType, computed, watch} from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})

const seriesList:any = computed(() => {
  return props.optionData.series
})
</script>
