<template>
  <command :name="command">
    <icon :type="iconType" />
  </command>
</template>

<script>
import { Command } from 'vg-editor'
import Icon from '../Icon'

export default {
  name: 'ButtonItem',
  components: {
    Command,
    Icon
  },
  props: ['command', 'icon', 'text'],
  computed: {
    iconType() {
      return `icon-${this.icon || this.command}`
    }
  }
}
</script>
