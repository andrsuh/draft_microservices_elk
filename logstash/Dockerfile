FROM logstash-with-jmx-plugin

COPY logstash.conf /config-dir/
COPY extended-stack-trace-formatter.rb /config-dir/
COPY jmxconf /config-dir/jmxconf/

CMD ["-f", "/config-dir/logstash.conf"]