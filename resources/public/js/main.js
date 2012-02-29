void function() {
  // Hidden
  $('.hidden').hide().removeClass('hidden')

  // Highlight Coads
  SyntaxHighlighter.defaults['gutter'] = false
  SyntaxHighlighter.defaults['light'] = true
  SyntaxHighlighter.defaults['quick-code'] = false
  SyntaxHighlighter.defaults['tab-size'] = 2
  SyntaxHighlighter.defaults['toolbar'] = false
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
}()

