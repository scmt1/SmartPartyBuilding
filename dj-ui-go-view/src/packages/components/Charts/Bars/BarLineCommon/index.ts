import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const BarLineCommonConfig: ConfigType = {
  key: 'BarLineCommon',
  chartKey: 'VBarLineCommon',
  conKey: 'VCBarLineCommon',
  title: '柱状折线图',
  category: ChatCategoryEnum.BAR,
  categoryName: ChatCategoryEnumName.BAR,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.ECHARTS,
  image: 'bar-line.png'
}
