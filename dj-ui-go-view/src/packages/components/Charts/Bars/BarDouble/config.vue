<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="双柱立体" :expanded="true">
    <SettingItemBox name="颜色">
      <setting-item name="内部柱子顶部1">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="内部柱子顶部2">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间柱子1（上半）">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].itemStyle.color.colorStops[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间柱子1（下半）">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].itemStyle.color.colorStops[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间柱子2（上半）">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].itemStyle.color.colorStops[0].color"></n-color-picker>
      </setting-item>
      <setting-item name="中间柱子2（下半）">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].itemStyle.color.colorStops[1].color"></n-color-picker>
      </setting-item>
      <setting-item name="背景柱子1">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[4].itemStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="背景柱子2">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[5].itemStyle.normal.color"></n-color-picker>
      </setting-item>
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
    <SettingItemBox name="柱子1标签">
      <setting-item name="显示">
        <n-space>
          <n-switch v-model:value="seriesList[2].label.show" size="small" />
        </n-space>
      </setting-item>
      <setting-item name="大小">
        <n-input-number v-model:value="seriesList[2].label.fontSize" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].label.color"></n-color-picker>
      </setting-item>
      <setting-item name="位置">
        <n-select
            v-model:value="seriesList[2].label.position"
            :options="[
            { label: 'top', value: 'top' },
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' },
            { label: 'bottom', value: 'bottom' }
          ]"
        />
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="柱子2标签">
      <setting-item name="显示">
        <n-space>
          <n-switch v-model:value="seriesList[3].label.show" size="small" />
        </n-space>
      </setting-item>
      <setting-item name="大小">
        <n-input-number v-model:value="seriesList[3].label.fontSize" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].label.color"></n-color-picker>
      </setting-item>
      <setting-item name="位置">
        <n-select
            v-model:value="seriesList[3].label.position"
            :options="[
            { label: 'top', value: 'top' },
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' },
            { label: 'bottom', value: 'bottom' }
          ]"
        />
      </setting-item>
    </SettingItemBox>


    <SettingItemBox name="圆角">
      <SettingItem name="柱子1">
        <n-input-number v-model:value="seriesList[2].itemStyle.borderRadius" :min="0" size="small"></n-input-number>
      </SettingItem>
      <SettingItem name="柱子2">
        <n-input-number v-model:value="seriesList[3].itemStyle.borderRadius" :min="0" size="small"></n-input-number>
      </SettingItem>
    </SettingItemBox>
  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})

const seriesList = computed(() => {
  return props.optionData.series
})
</script>
