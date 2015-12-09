key = 1
table_name = (0...10).map { ('a'..'z').to_a[rand(26)] }.join
puts "Created table #{table_name}"
res = `curl -sS -X POST $HOST/#{table_name}`
while true do
  res = `curl -sS -X POST $HOST/#{table_name}/foo#{key}/bar#{key}`
  puts res unless res.empty?
  res = `curl -sS -X POST $HOST/#{table_name}/fe#{key}/baz#{key}`
  puts res unless res.empty?
  print '.'
  if key > 10
    current = `curl -sS $HOST/#{table_name}/foo#{key-10}`
    if (current != "bar#{key-10}")
      puts "Read problems foo#{key-10} - #{current} "
    end
  end
  sleep 0.2
  key += 1
end
