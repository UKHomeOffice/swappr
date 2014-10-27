require 'capybara/rspec'
require 'capybara/poltergeist'
require 'pry'

Capybara.default_driver = :poltergeist

HOST = ENV['HOST'] || 'http://localhost:8080'
