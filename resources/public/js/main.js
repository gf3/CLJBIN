void function() {
  var i
    , l
    , $fork = document.querySelector('ul.actions li.fork-action a')
    , $code = document.getElementById('code')
    , slice = Array.prototype.slice

  // Hidden
  var hidden = slice.call(document.getElementsByClassName('hidden'))
  for (i = 0, l = hidden.length; i < l; i ++) {
    hidden[i].style.display = 'none'
    hidden[i].classList.remove('hidden')
  }

  // Highlight Coads
  SyntaxHighlighter.defaults['gutter']      = false
  SyntaxHighlighter.defaults['light']       = true
  SyntaxHighlighter.defaults['quick-code']  = false
  SyntaxHighlighter.defaults['tab-size']    = 2
  SyntaxHighlighter.defaults['toolbar']     = false
  SyntaxHighlighter.all()
  SyntaxHighlighter.highlight()

  // Fork
  if ($fork)
    $fork.addEventListener('click', function(e) {
      var $this = e.currentTarget

      $code.style.display = 'block'
      $code.select()

      $this.style.display = 'none'
      $this.parentNode.parentNode.querySelector('.paste-action').style.display = 'inline'

      e.preventDefault()
    }, false)

  // Run
  // TODO: It

  // Editor
  if ($code) {
    $code.addEventListener('keydown', function(e) {
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
    }, false)
    $code.focus()
  }
}()

