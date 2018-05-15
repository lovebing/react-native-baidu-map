require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name           = 'RCTBaiduMap'
  s.version        = package['version'].gsub(/v|-beta/, '')
  s.summary        = package['description']
  s.author         = package['author']
  s.license        = package['license']
  s.homepage       = package['homepage']

  s.platform     = :ios, "9.0"

  s.source       = { :git => "https://github.com/futurechallenger/react-native-baidu-map.git", :tag => "#{s.version}" }
  s.source_files  = "ios/RCTBaiduMap/*.{h,m}"
  s.public_header_files = "ios/RCTBaiduMap/*.h"

  s.ios.deployment_target = '9.0'
  s.preserve_paths = '*.js'
  
  s.dependency 'BaiduMapKit'

end
