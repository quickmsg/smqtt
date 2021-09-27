<template>
  <command :name="command">
    <div class="item">
      <icon :type="iconType" />
      <span>{{ label }}</span>
    </div>
  </command>
</template>

<script>
import { Command } from 'vg-editor'
import Icon from '../Icon'
import upperFirst from 'lodash/upperFirst'

export default {
  name: 'MenuItem',
  components: {
    Command,
    Icon
  },
  props: ['command', 'icon', 'text'],
  computed: {
    iconType() {
      return `icon-${this.icon || this.command}`
    },
    label() {
      return this.text || upperFirst(this.command)
    }
  }
}
</script>
