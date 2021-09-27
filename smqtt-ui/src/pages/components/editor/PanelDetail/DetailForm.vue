<template>
    <a-form @submit.prevent="handleSubmit">
      <a-form-item v-if="type === 'node'">
        <a-input v-model="formModel.label" @blur.prevent="handleSubmit" />
      </a-form-item>
      <a-form-item v-else-if="type === 'edge'">
        <a-input v-model="formModel.label" @blur.prevent="handleSubmit"/>
        <a-select v-model="formModel.shape" @change.prevent="handleSubmit">
          <a-select-option value="flow-smooth">Smooth</a-select-option>
          <a-select-option value="flow-polyline">Polyline</a-select-option>
          <a-select-option value="custom-polyline">Custom Polyline</a-select-option>
          <a-select-option value="flow-polyline-round">Polyline Round</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item v-else-if="type === 'group'">
        <a-input v-model="formModel.label" @blur.prevent="handleSubmit" />
      </a-form-item>
    </a-form>
</template>

<script>
import { omit } from 'lodash'

export default {
  name: 'DetailForm',
  inject: ['root'],
  props: ['type'],
  data() {
    return {
      formModel: {}
    }
  },
  created() {
    const formModel = this.root.propsAPI.getSelected()[0].getModel()
    this.formModel = Object.assign({}, { shape: 'flow-smooth' }, formModel)
  },
  methods: {
    handleSubmit(e) {
      const { getSelected, executeCommand, update } = this.root.propsAPI
      const { formModel } = this
      setTimeout(() => {
        const item = getSelected()[0]
        if (!item) return
        executeCommand(() => {
          update(item, { ...omit(formModel, 'children') })
        })
      }, 0)
    }
  }
}
</script>
