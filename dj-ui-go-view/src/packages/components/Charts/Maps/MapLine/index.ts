import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const MapLineConfig: ConfigType = {
    key: 'MapLine',
    chartKey: 'VMapLine',
    conKey: 'VCMapLine',
    title: '地图迁徙',
    category: ChatCategoryEnum.MAP,
    categoryName: ChatCategoryEnumName.MAP,
    package: PackagesCategoryEnum.CHARTS,
    chartFrame: ChartFrameEnum.COMMON,
    image: 'mapLine.png'
}
