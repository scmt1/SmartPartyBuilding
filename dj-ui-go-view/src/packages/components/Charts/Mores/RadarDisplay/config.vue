<template>
  <div>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
    <CollapseItem name="时间线相关" :expanded="true">
      <SettingItemBox name="时间线">
        <SettingItem name="显示时间线">
          <n-space>
            <n-switch v-model:value="optionData.baseOption.timeline.show" size="small"></n-switch>
          </n-space>
        </SettingItem>
        <SettingItem name="自动播放">
          <n-space>
            <n-switch v-model:value="optionData.baseOption.timeline.autoPlay" size="small"></n-switch>
          </n-space>
        </SettingItem>
        <SettingItem name="播放速度(毫秒值)">
          <n-space>
            <n-input-number
                v-model:value="optionData.baseOption.timeline.playInterval"
                size="small"
                :min="0"
                :step="20"
            ></n-input-number>
          </n-space>
        </SettingItem>
        <setting-item name="播放按钮的位置">
          <n-select
              v-model:value="optionData.baseOption.timeline.controlPosition"
              :options="[
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' }
          ]"
          />
        </setting-item>
        <setting-item name="距离底部的距离">
          <n-slider
              v-model:value="optionData.baseOption.timeline.bottom"
              :min="0"
              :max="100"
              :format-tooltip="sliderFormatTooltip"
          ></n-slider>
        </setting-item>
      </SettingItemBox>
    </CollapseItem>
    <CollapseItem name="图例调整" :expanded="true">
      <SettingItemBox name="图例位置">
        <SettingItem name="上下偏移">
          <n-slider
              v-model:value="optionData.baseOption.legend.top"
              :max="100"
          ></n-slider>
        </SettingItem>
        <SettingItem name="左右偏移">
          <n-slider
              v-model:value="optionData.baseOption.legend.left"
              :max="100"
          ></n-slider>
        </SettingItem>
        <SettingItem name="文字颜色">
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.baseOption.legend.textStyle.color"></n-color-picker>

        </SettingItem>
        <SettingItem name="文字大小">
          <n-slider
              v-model:value="optionData.baseOption.legend.textStyle.fontSize"
              :max="100"
          ></n-slider>
        </SettingItem>
      </SettingItemBox>
    </CollapseItem>
    <CollapseItem v-for="(item, index) in optionData.baseOption.series" :key="index" :name="`雷达图-${index + 1}`" :expanded="true">
      <SettingItemBox>
        <SettingItem name="渐变色1">
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.baseOption.series[index].areaStyle.color.colorStops[0].color"></n-color-picker>
        </SettingItem>
        <SettingItem name="渐变色2">
          <n-color-picker size="small" :modes="['hex']" v-model:value="optionData.baseOption.series[index].areaStyle.color.colorStops[1].color"></n-color-picker>
        </SettingItem>
      </SettingItemBox>
    </CollapseItem>

  </div>
</template>

<script setup lang="ts">
import { PropType, computed, reactive } from 'vue'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { option, RadarShapeEnumList } from './config'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'

const props = defineProps({
  optionData: {
    type: Object as PropType<typeof option & GlobalThemeJsonType>,
    required: true
  }
});



const radarProp = reactive({
  radius: [0, 60],
  center: [50, 50]
})

// 更新处理
const updateRadius0 = (value: number) => {
  props.optionData.radar.radius[0] = `${value}%`
}

const updateRadius1 = (value: number) => {
  props.optionData.radar.radius[1] = `${value}%`
}

// 更新处理
const updateCenter0 = (value: number) => {
  props.optionData.radar.center[0] = `${value}%`
}

const updateCenter1 = (value: number) => {
  props.optionData.radar.center[1] = `${value}%`
}

// 百分比格式化 percent
const sliderFormatTooltip = (v: number) => {
  return `${v}%`
}
</script>
