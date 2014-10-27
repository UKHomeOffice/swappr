require 'capybara/rspec'
require 'capybara/poltergeist'
require 'pry'

Capybara.register_driver :poltergeist do |app|
  Capybara::Poltergeist::Driver.new(app, {
    :js_errors => false
  })
end

Capybara.default_driver = :poltergeist

HOST = ENV['HOST'] || 'http://localhost:8080'
