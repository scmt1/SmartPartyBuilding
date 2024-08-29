<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="单折线面积图" :expanded="true">
    <SettingItemBox name="线条">
      <SettingItem name="宽度">
        <n-input-number
          v-model:value="seriesList[0].lineStyle.width"
          :min="1"
          :max="100"
          size="small"
          placeholder="自动计算"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="类型">
        <n-select v-model:value="seriesList[0].lineStyle.type" size="small" :options="lineConf.lineStyle.type"></n-select>
      </SettingItem>
      <setting-item name="拐点的颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.color"></n-color-picker>
      </setting-item>
      <setting-item name="折线颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].lineStyle.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].areaStyle.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积透明度，取0~1之间的数字，默认0.8">
        <n-input-number v-model:value="seriesList[0].areaStyle.opacity" size="small" :min="0" :max="1"></n-input-number>
      </setting-item>
    </SettingItemBox>
    <setting-item-box name="标签">
      <setting-item>
        <n-space>
          <n-switch v-model:value="seriesList[0].label.show" size="small" />
          <n-text>展示标签</n-text>
        </n-space>
      </setting-item>
      <setting-item name="大小">
        <n-input-number v-model:value="seriesList[0].label.fontSize" size="small" :min="1"></n-input-number>
      </setting-item>
      <setting-item name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].label.color"></n-color-picker>
      </setting-item>
      <setting-item name="位置">
        <n-select
          v-model:value="seriesList[0].label.position"
          :options="[
            { label: 'top', value: 'top' },
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' },
            { label: 'bottom', value: 'bottom' }
          ]"
        />
      </setting-item>


    </setting-item-box>
    <setting-item-box name="特效">
      <setting-item>
        <n-space>
          <n-switch v-model:value="seriesList[1].effect.show" size="small" />
          <n-text>展示特效</n-text>
        </n-space>
      </setting-item>
      <setting-item name="特效尾迹的长度。取从 0 到 1 的值，数值越大尾迹越长">
        <n-input-number v-model:value="seriesList[1].effect.trailLength" size="small" :max="1"></n-input-number>
      </setting-item>
      <setting-item name="特效动画的时间，单位为 s。">
        <n-input-number v-model:value="seriesList[1].effect.period" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效大小">
        <n-input-number v-model:value="seriesList[1].effect.symbolSize" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].lineStyle.normal.color"></n-color-picker>
      </setting-item>

    </setting-item-box>
  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { lineConf } from '@/packages/chartConfiguration/echarts/index'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'

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
