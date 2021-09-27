import './iconfont'

export default {
  name: 'Icon',

  props: ['type'],

  render() {
    const { type } = this
    return (
      <i class='action'>
        <svg width='1em' height='1em' fill='currentColor'>
          <use xlinkHref={`#${type}`}/>
        </svg>
      </i>
    )
  }
}
