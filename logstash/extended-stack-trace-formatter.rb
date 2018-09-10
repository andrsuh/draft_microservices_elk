def register(params)
  params = params.dup

  @source = params.delete('source') || fail(LogStash::ConfigurationError, 'no `source` provided!')
  @target = params.delete('target') || fail(LogStash::ConfigurationError, 'no `target` provided!')

  @joiner = params.delete('joiner') || "\n"
  @indent = params.delete('indent') || "\t"

  params.empty? || fail(LogStash::ConfigurationError, "unknown params #{params.inspect}")
end

def filter(event)
  thrown = event.get(@source)
  return if thrown.nil?

  event.set(@target, stacktrace_format(thrown))
rescue => e
  logger.warn("Exception while formatting extended stacktrace", :exception => e.message, :backtrace => e.backtrace)
  event.tag('_stacktraceformaterror')
end

def stacktrace_format(thrown)
  message = []
  message << thrown.fetch('localizedMessage') { thrown.fetch('message', '?')}
  thrown['extendedStackTrace'].each do |entry|
    message << "#{@indent}at #{entry.fetch('class','?')}.#{entry.fetch('method','?')}(#{entry.fetch('file','?')}:#{entry.fetch('line','?')}) ~[#{entry.fetch('location','?')}:#{entry.fetch('version','?')}]"
  end
  if thrown.include?('cause')
    message << 'Caused by:'
    message << stacktrace_format(thrown['cause'])
  end
  message.join(@joiner)
end