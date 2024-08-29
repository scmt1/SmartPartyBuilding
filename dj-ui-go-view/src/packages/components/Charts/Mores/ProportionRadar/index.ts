import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const ProportionRadarConfig: ConfigType = {
  key: 'ProportionRadar',
  chartKey: 'VProportionRadar',
  conKey: 'VCProportionRadar',
  title: '占比及数量雷达图',
  category: ChatCategoryEnum.MORE,
  categoryName: ChatCategoryEnumName.MORE,
  package: PackagesCategoryEnum.CHARTS,
  chartFrame: ChartFrameEnum.COMMON,
  image: 'proportionRadar.png'
}
