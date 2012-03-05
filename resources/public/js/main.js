void function() {
  // Hidden
  $('.hidden').hide().removeClass('hidden')

  // Highlight Coads
  SyntaxHighlighter.defaults['gutter']      = false
  SyntaxHighlighter.defaults['light']       = true
  SyntaxHighlighter.defaults['quick-code']  = false
  SyntaxHighlighter.defaults['tab-size']    = 2
  SyntaxHighlighter.defaults['toolbar']     = false
  SyntaxHighlighter.all()

  // Fork
  $('ul.actions li.fork-action a').click(function(e) {
    var $this = $(this)

    $('#code')
      .show()

    $this
      .hide()
      .parents('ul').find('.paste-action').show()

    e.preventDefault()
  })

  // Run
  // TODO: It

  // Editor
  $('#code').on('keydown', function(e) {
    if (e.keyCode === 9) {
      e.preventDefault()
      this.value += '  '
    }
    // else if (this.selectionStart && this.setSelectionRange && e.keyCode === 8)
      // if (this.selectionStart >= 2)
        // if (this.value.substr(this.selectionStart - 2, 2) === '  ') {
          // e.preventDefault()
          // this.value = this.value.substring(0, this.selectionStart - 2) + this.value.substring(this.selectionStart)
          // this.setSelectionRange(this.selectionStart, this.selectionStart)
        // }
  }).focus()
}()

