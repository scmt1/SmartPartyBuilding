<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="图例调整" :expanded="true">
    <SettingItemBox name="图例调整">
      <SettingItem>
        <n-space>
          <n-input-number
              v-model:value="legend.textStyle.fontSize"
              size="small"
              :min="2"
          ></n-input-number>
          <n-text>图例字体大小</n-text>
        </n-space>
      </SettingItem>

      <setting-item>
        <n-space>
          <n-input-number v-model:value="legend.padding" size="small" />
          <n-text>图例内边距</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-input-number v-model:value="legend.itemHeight" size="small" />
          <n-text>图例图标高度</n-text>
        </n-space>
      </setting-item>
      <setting-item>
        <n-space>
          <n-input-number v-model:value="legend.itemWidth" size="small" />
          <n-text>图例图标宽度</n-text>
        </n-space>
      </setting-item>
    </SettingItemBox>
  </CollapseItem>
  <CollapseItem name="折线图1" :expanded="true">
    <SettingItemBox name="线条">
      <SettingItem name="线条宽度">
        <n-input-number
            v-model:value="seriesList[0].lineStyle.normal.width"
            :min="1"
            :max="100"
            size="small"
            placeholder="自动计算"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="类型">
        <n-select v-model:value="seriesList[0].lineStyle.normal.type" size="small" :options="lineConf.lineStyle.type"></n-select>
      </SettingItem>
      <setting-item name="折线颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].lineStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].areaStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积透明度，取0~1之间的数字，默认0.8">
        <n-input-number v-model:value="seriesList[0].areaStyle.normal.opacity" size="small" :min="0" :max="1"></n-input-number>
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="节点">
      <SettingItem name="大小">
        <n-input-number
            v-model:value="seriesList[0].symbolSize"
            :min="1"
            :max="100"
            size="small"
            placeholder="自动计算"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="类型">
        <n-select v-model:value="seriesList[0].lineStyle.normal.type" size="small" :options="lineConf.lineStyle.type"></n-select>
      </SettingItem>
      <setting-item name="拐点的颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.color"></n-color-picker>
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="标签">
      <SettingItem name="展示标签">
        <n-switch v-model:value="seriesList[0].label.show" size="small" />
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number v-model:value="seriesList[0].label.fontSize" size="small" :min="1"></n-input-number>
      </SettingItem>
      <setting-item name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].label.textStyle.color"></n-color-picker>
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
    </SettingItemBox>
    <setting-item-box name="特效">
      <setting-item>
        <n-space>
          <n-switch v-model:value="seriesList[3].effect.show" size="small" />
          <n-text>展示特效</n-text>
        </n-space>
      </setting-item>
      <setting-item name="特效尾迹的长度。取从 0 到 1 的值，数值越大尾迹越长">
        <n-input-number v-model:value="seriesList[3].effect.trailLength" size="small" :max="1"></n-input-number>
      </setting-item>
      <setting-item name="特效动画的时间，单位为 s。">
        <n-input-number v-model:value="seriesList[3].effect.period" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效大小">
        <n-input-number v-model:value="seriesList[3].effect.symbolSize" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[3].lineStyle.normal.color"></n-color-picker>
      </setting-item>
    </setting-item-box>
  </CollapseItem>
  <CollapseItem name="折线图2" :expanded="true">
    <SettingItemBox name="线条">
      <SettingItem name="线条宽度">
        <n-input-number
            v-model:value="seriesList[1].lineStyle.normal.width"
            :min="1"
            :max="100"
            size="small"
            placeholder="自动计算"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="类型">
        <n-select v-model:value="seriesList[1].lineStyle.normal.type" size="small" :options="lineConf.lineStyle.type"></n-select>
      </SettingItem>
      <setting-item name="折线颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].lineStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].areaStyle.normal.color"></n-color-picker>
      </setting-item>
      <setting-item name="区域面积透明度，取0~1之间的数字，默认0.8">
        <n-input-number v-model:value="seriesList[1].areaStyle.normal.opacity" size="small" :min="0" :max="1"></n-input-number>
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="节点">
      <SettingItem name="大小">
        <n-input-number
            v-model:value="seriesList[1].symbolSize"
            :min="1"
            :max="100"
            size="small"
            placeholder="自动计算"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="类型">
        <n-select v-model:value="seriesList[1].lineStyle.normal.type" size="small" :options="lineConf.lineStyle.type"></n-select>
      </SettingItem>
      <setting-item name="拐点的颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].itemStyle.color"></n-color-picker>
      </setting-item>
    </SettingItemBox>
    <SettingItemBox name="标签">
      <SettingItem name="展示标签">
        <n-switch v-model:value="seriesList[1].label.show" size="small" />
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number v-model:value="seriesList[1].label.fontSize" size="small" :min="1"></n-input-number>
      </SettingItem>
      <setting-item name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[1].label.textStyle.color"></n-color-picker>
      </setting-item>
      <setting-item name="位置">
        <n-select
            v-model:value="seriesList[1].label.position"
            :options="[
            { label: 'top', value: 'top' },
            { label: 'left', value: 'left' },
            { label: 'right', value: 'right' },
            { label: 'bottom', value: 'bottom' }
          ]"
        />
      </setting-item>
    </SettingItemBox>
    <setting-item-box name="特效">
      <setting-item>
        <n-space>
          <n-switch v-model:value="seriesList[2].effect.show" size="small" />
          <n-text>展示特效</n-text>
        </n-space>
      </setting-item>
      <setting-item name="特效尾迹的长度。取从 0 到 1 的值，数值越大尾迹越长">
        <n-input-number v-model:value="seriesList[2].effect.trailLength" size="small" :max="1"></n-input-number>
      </setting-item>
      <setting-item name="特效动画的时间，单位为 s。">
        <n-input-number v-model:value="seriesList[2].effect.period" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效大小">
        <n-input-number v-model:value="seriesList[2].effect.symbolSize" size="small" :min="0"></n-input-number>
      </setting-item>
      <setting-item name="特效颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].lineStyle.normal.color"></n-color-picker>
      </setting-item>
    </setting-item-box>
  </CollapseItem>

</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { lineConf } from '@/packages/chartConfiguration/echarts/index'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import {option} from "@/packages/components/Informations/Mores/WordCloud/config";

const props:any = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})
const legend = computed(() => {
  return props.optionData.legend
})

const seriesList = computed(() => {
  return props.optionData.series
})
</script>
