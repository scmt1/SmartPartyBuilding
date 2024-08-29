import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarBeamAConfig: ConfigType = {
  key: 'BarBeamA',
  chartKey: 'VBarBeamA',
  conKey: 'VCBarBeamA',
  title: '光柱图类型A',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'beam_A.png'
}
