<template>
  <component :is="activeComponent" />
</template>

<script>
import MindContextMenu from './MindContextMenu'
import PublicContextMenu from './PublicContextMenu'

export default {
  name: 'ContextMenu',
  components: {
    MindContextMenu,
    PublicContextMenu
  },
  props: {
    graphType: {
      type: String,
      required: true
    }
  },
  computed: {
    activeComponent() {
      const { graphType } = this
      if (graphType === 'mind') return MindContextMenu
      return PublicContextMenu
    }
  }
}
</script>
