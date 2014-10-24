require 'capybara/rspec'
require 'capybara/poltergeist'

Capybara.default_driver = :selenium

RSpec.configure do |config|
  HOST = ENV['HOST'] || 'http://localhost:8080'
end