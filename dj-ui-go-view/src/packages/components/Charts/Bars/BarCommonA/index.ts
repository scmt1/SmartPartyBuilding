import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarCommonAConfig: ConfigType = {
  key: 'BarCommonA',
  chartKey: 'VBarCommonA',
  conKey: 'VCBarCommonA',
  title: '柱状图类型A',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar_A.png'
}
