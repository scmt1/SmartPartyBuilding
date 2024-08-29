import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarBeamBConfig: ConfigType = {
  key: 'BarBeamB',
  chartKey: 'VBarBeamB',
  conKey: 'VCBarBeamB',
  title: '光柱图类型B',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'beam_B.png'
}
