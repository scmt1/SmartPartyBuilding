<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem :name="`内部柱状颜色`" :expanded="true">
    <SettingItemBox name="渐变颜色">
      <SettingItem name="数值">
        <n-input-number v-model:value="config.series[2].data[0].value" size="small" :min="0" placeholder="进度值"></n-input-number>
      </SettingItem>
      <SettingItem name="圆柱顶部颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[0].data[0].itemStyle.normal.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="圆柱底部颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[1].data[0].itemStyle.normal.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="柱子对应数值颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[2].data[0].label.normal.textStyle.color"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="柱子渐变颜色">
      <SettingItem name="0%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[2].data[0].itemStyle.normal.color.colorStops[0].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="100%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[2].data[0].itemStyle.normal.color.colorStops[1].color"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>
  </CollapseItem>
  <CollapseItem :name="`外部柱状颜色`" :expanded="true">
    <SettingItemBox name="渐变颜色">
      <SettingItem name="圆柱顶部颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[3].data[0].itemStyle.normal.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="圆柱底部颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[4].data[0].itemStyle.normal.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="柱子底部内圈颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[5].data[0].itemStyle.normal.borderColor"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="柱子底部外圈颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[6].data[0].itemStyle.normal.borderColor"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="柱子底部渐变颜色">
      <SettingItem name="0%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[7].data[0].itemStyle.normal.color.colorStops[0].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="30%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[7].data[0].itemStyle.normal.color.colorStops[1].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="50%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[7].data[0].itemStyle.normal.color.colorStops[2].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="80%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[7].data[0].itemStyle.normal.color.colorStops[3].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="100%处的颜色">
        <n-color-picker
                size="small"
                :modes="['hex']"
                v-model:value="config.series[7].data[0].itemStyle.normal.color.colorStops[4].color"
        ></n-color-picker>
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

const config = computed(() => {
  return props.optionData
})
</script>
