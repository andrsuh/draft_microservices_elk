FROM logstash

COPY logstash.conf /config-dir/
COPY extended-stack-trace-formatter.rb /config-dir/

CMD ["-f", "/config-dir/logstash.conf"]