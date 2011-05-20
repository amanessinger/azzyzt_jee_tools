Deplate codes a fixed '--wrap' into the commandline of "highlight". In
case of XML, this leads to wrongly highlit text. I found no other way
than to patch the installation of "deplate".



--- code-highlight.rb	2011-05-16 15:12:24.234701870 +0200
+++ /var/lib/gems/1.8/gems/deplate-0.8.5/lib/deplate/mod/code-highlight.rb	2011-05-16 15:13:00.204701869 +0200
@@ -91,7 +91,11 @@
             style = %{--style=#{style}}
         end
         tw  = @deplate.variables["tabwidth"] || 4
-        cmd = "#{Deplate::External.get_app('highlight')} --fragment --wrap --replace-tabs=#{tw} --syntax #{syntax} --style-outfile=#{style_out} #{style} #{args.join(' ')}"
+        #
+        # eliminated --wrap (andreas, 16-MAY-2011)
+        #
+        # cmd = "#{Deplate::External.get_app('highlight')} --fragment --wrap --replace-tabs=#{tw} --syntax #{syntax} --style-outfile=#{style_out} #{style} #{args.join(' ')}"
+        cmd = "#{Deplate::External.get_app('highlight')} --fragment --replace-tabs=#{tw} --syntax #{syntax} --style-outfile=#{style_out} #{style} #{args.join(' ')}"
         Deplate::External.log_popen(self, cmd) do |io|
             io.puts(text)
             io.close_write
