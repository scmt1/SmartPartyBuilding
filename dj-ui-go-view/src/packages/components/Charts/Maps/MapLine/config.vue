<template>
  <!-- Echarts 全局设置 -->
  <global-setting :optionData="optionData"></global-setting>
  <CollapseItem name="地图" :expanded="true">
    <SettingItemBox name="地图区域">
      <SettingItem name="默认中国">
        <n-select
          size="small"
          v-model:value="mapRegion.adcode"
          :options="mapRegionOptions"
          value-field="adcode"
          label-field="name"
        />
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="区域颜色">
      <SettingItem name="0%处颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].itemStyle.areaColor.colorStops[0].color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="100%处颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].itemStyle.areaColor.colorStops[1].color"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="阴影">
      <SettingItem name="颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].itemStyle.shadowColor"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="模糊程度">
        <n-input-number
          v-model:value="seriesList[1].itemStyle.shadowBlur"
          :min="0"
          size="small"
          placeholder="请输入模糊程度"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="水平偏移">
        <n-input-number
          v-model:value="seriesList[1].itemStyle.shadowOffsetX"
          size="small"
          placeholder="请输入水平偏移大小"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="垂直偏移">
        <n-input-number
          v-model:value="seriesList[1].itemStyle.shadowOffsetY"
          size="small"
          placeholder="请输入垂直偏移大小"
        ></n-input-number>
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="地理信息名称">
      <SettingItem name="显示">
        <n-space>
          <n-switch v-model:value="seriesList[1].label.show" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="字体颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].label.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number
          v-model:value="seriesList[1].label.fontSize"
          :min="1"
          size="small"
          placeholder="请输入字体大小"
        ></n-input-number>
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="悬浮 （预览可见）">
      <SettingItem name="禁用">
        <n-space>
          <n-switch v-model:value="seriesList[1].emphasis.disabled" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].emphasis.itemStyle.areaColor"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number
          v-model:value="seriesList[1].emphasis.label.fontSize"
          :min="1"
          size="small"
          placeholder="请输入字体大小"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="阴影">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].emphasis.itemStyle.shadowColor"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="边框大小">
        <n-input-number
          v-model:value="seriesList[1].emphasis.itemStyle.borderWidth"
          :min="1"
          size="small"
          placeholder="请输入边框大小"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="文字颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].emphasis.label.color"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="悬浮弹窗">
      <SettingItem name="显示">
        <n-space>
          <n-switch v-model:value="seriesList[1].tooltip.show" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number
          v-model:value="seriesList[1].tooltip.textStyle.fontSize"
          :min="1"
          size="small"
          placeholder="请输入字体大小"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="字体颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].tooltip.textStyle.color"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="背景颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].tooltip.backgroundColor"
        ></n-color-picker>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="区域边框">
      <SettingItem name="颜色">
        <n-color-picker
          size="small"
          :modes="['hex']"
          v-model:value="seriesList[1].itemStyle.borderColor"
        ></n-color-picker>
      </SettingItem>
      <SettingItem name="宽度大小">
        <n-input-number
          v-model:value="seriesList[1].itemStyle.borderWidth"
          :min="1"
          size="small"
          placeholder="请输入边框大小"
        ></n-input-number>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="其他" v-if="mapRegion.adcode === 'china'">
      <SettingItem>
        <n-checkbox v-model:checked="mapRegion.showHainanIsLands" size="small">显示南海群岛</n-checkbox>
      </SettingItem>
    </SettingItemBox>
  </CollapseItem>
  <CollapseItem name="标记" :expanded="true">
    <SettingItemBox name="样式">
      <SettingItem name="标记大小">
        <n-input-number v-model:value="seriesList[0].symbolSize" size="small" :min="0"></n-input-number>
      </SettingItem>
      <SettingItem name="颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.color"></n-color-picker>
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="涟漪文本">
      <SettingItem name="显示">
        <n-space>
          <n-switch v-model:value="seriesList[0].label.show" size="small"></n-switch>
        </n-space>
      </SettingItem>
      <SettingItem name="字体大小">
        <n-input-number v-model:value="seriesList[0].label.fontSize" size="small" :min="0"></n-input-number>
      </SettingItem>
      <SettingItem name="字体颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].label.color"></n-color-picker>
      </SettingItem>
      <SettingItem name="涟漪文字的位置">
        <n-select size="small" v-model:value="seriesList[0].label.position" :options="positionOptions" />
      </SettingItem>
    </SettingItemBox>

    <SettingItemBox name="涟漪配置">
      <SettingItem name="涟漪的配色方案">
        <n-select size="small" v-model:value="seriesList[0].colorBy" :options="colorByOptions" />
      </SettingItem>
      <SettingItem name="图例hover时的联动高亮">
        <n-switch v-model:value="seriesList[0].legendHoverLink" size="small"></n-switch>
      </SettingItem>
      <SettingItem name="涟漪大小">
        <n-input-number
          v-model:value="seriesList[0].rippleEffect.scale"
          :min="1"
          size="small"
          placeholder="请输入涟漪大小"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="涟漪颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].rippleEffect.color"></n-color-picker>
      </SettingItem>
      <SettingItem name="波纹的数量">
        <n-input-number
            v-model:value="seriesList[0].rippleEffect.number"
            :min="1"
            size="small"
            placeholder="请输入波纹的数量"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="动画的周期（秒）">
        <n-input-number
            v-model:value="seriesList[0].rippleEffect.period"
            size="small"
            placeholder="请输入周期（秒）"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="涟漪的绘制方式">
        <n-select size="small" v-model:value="seriesList[0].rippleEffect.brushType" :options="rippleEffectOptions" />
      </SettingItem>
      <SettingItem name="图形的描边颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.borderColor"></n-color-picker>
      </SettingItem>
      <SettingItem name="描边线宽">
        <n-input-number
            v-model:value="seriesList[0].itemStyle.borderWidth"
            size="small"
            placeholder="为 0 时无描边"
        ></n-input-number>
      </SettingItem>
      <SettingItem name="图形的阴影颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[0].itemStyle.shadowColor"></n-color-picker>
      </SettingItem>
      <SettingItem name="图形阴影的模糊大小">
        <n-input-number
            v-model:value="seriesList[0].itemStyle.shadowBlur"
            size="small"
            placeholder="图形阴影的模糊大小"
        ></n-input-number>
      </SettingItem>
    </SettingItemBox>
  </CollapseItem>
  <CollapseItem name="迁徙线" :expanded="true">
    <SettingItemBox name="标签">
      <SettingItem name="是否显示标签">
        <n-switch v-model:value="seriesList[2].label.show" size="small"></n-switch>
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="特效">
      <SettingItem name="是否显示特效">
        <n-switch v-model:value="seriesList[2].effect.show" size="small"></n-switch>
      </SettingItem>
      <SettingItem name="迁徙指向速度">
        <n-input-number v-model:value="seriesList[2].effect.period" size="small" :min="0"></n-input-number>
      </SettingItem>
      <SettingItem name="特效尾迹长度">
        <n-input-number v-model:value="seriesList[2].effect.trailLength" size="small"></n-input-number>
      </SettingItem>
      <SettingItem name="特效标记的颜色">
        <n-color-picker size="small" :modes="['hex']" v-model:value="seriesList[2].effect.color"></n-color-picker>
      </SettingItem>
      <SettingItem name="特效标记大小">
        <n-input-number v-model:value="seriesList[2].effect.symbolSize" size="small"></n-input-number>
      </SettingItem>
      <SettingItem name="箭头图标(更改后需重新开关一次上方特效显示)">
        <n-select size="small" v-model:value="seriesList[2].effect.symbol" :options="symbolOptions" />
      </SettingItem>
    </SettingItemBox>
    <SettingItemBox name="线条">
      <SettingItem name="线条宽度">
        <n-input-number v-model:value="seriesList[2].lineStyle.normal.width" size="small"></n-input-number>
      </SettingItem>
      <SettingItem name="尾迹线条透明度">
        <n-input-number v-model:value="seriesList[2].lineStyle.normal.opacity" size="small"></n-input-number>
      </SettingItem>
      <SettingItem name="尾迹线条曲直度">
        <n-input-number v-model:value="seriesList[2].lineStyle.normal.curveness" size="small"></n-input-number>
      </SettingItem>
    </SettingItemBox>
  </CollapseItem>
</template>

<script setup lang="ts">
import { PropType, computed } from 'vue'
import { CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'
import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
import { GlobalSetting } from '@/components/Pages/ChartItemSetting'
import { ref } from 'vue'
import mapChinaJson from './mapGeojson/china.json'

const mapRegionOptions = ref([
  {
    adcode: 'china',
    name: '中国'
  }
])
const positionOptions = ref([
  {
    value: 'top',
    label: 'top'
  },
  {
    value: 'left',
    label: 'left'
  },
  {
    value: 'right',
    label: 'right'
  },
  {
    value: 'bottom',
    label: 'bottom'
  },
  {
    value: 'inside',
    label: 'inside'
  },
  {
    value: 'outside',
    label: 'outside'
  },

])
const colorByOptions = ref([
  {
    value: 'series',
    label: '调色盘'
  },
  {
    value: 'data',
    label: '数据项'
  }
])
const rippleEffectOptions = ref([
  {
    value: 'fill',
    label: '实心'
  },
  {
    value: 'stroke',
    label: '空心'
  }
])

const props = defineProps({
  optionData: {
    type: Object as PropType<GlobalThemeJsonType>,
    required: true
  }
})
const symbolOptions = ref([
  {
    value: 'circle',
    label: 'circle'
  },
  {
    value: 'rect',
    label: 'rect'
  },
  {
    value: 'roundRect',
    label: 'roundRect'
  },
  {
    value: 'triangle',
    label: 'triangle'
  },
  {
    value: 'diamond',
    label: 'diamond'
  },
  {
    value: 'pin',
    label: 'pin'
  },
  {
    value: 'arrow',
    label: 'arrow'
  },  {
    value: 'none',
    label: 'none'
  },

])

const initMapRegionOptions = () => {
  mapChinaJson.features.forEach((element: any) => {
    if (element.properties.name) {
      mapRegionOptions.value.push({ ...element.properties })
    }
  })
}
initMapRegionOptions()

const seriesList = computed(() => {
  return props.optionData.series
})

const mapRegion = computed(() => {
  return props.optionData.mapRegion
})
</script>
