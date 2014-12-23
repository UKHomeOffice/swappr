require 'spec_helper'
require 'securerandom'

class User
  def initialize(fullName)
    @username = 'test_' + SecureRandom.hex(8)
    @fullName = fullName
    @email = @username + '@example.com'
    @password = SecureRandom.hex(12)
  end
  def username
    @username
  end
  def fullName
    @fullName
  end
  def email
    @email
  end
  def password
    @password
  end
end

RSpec.configure do |config|
  config.before(:type => :feature) {
    $bill = User.new("Bill Beetroot")
    $ben = User.new("Ben Bernanke")
    $jeff = User.new("Jeff Jangles")

    create_user($bill)
    create_user($ben)
    create_user($jeff)
  }
end


def create_user(user)
  login_user_admin
  visit HOST + "/admin/users/add"
  fill_in('username', :with => user.username)
  fill_in('fullname', :with => user.fullName)
  fill_in('email', :with => user.email)
  fill_in('password', :with => user.password)
  fill_in('confirmPassword', :with => user.password)
  click_button('Add')
  logout
end

def login_user_admin
  visit HOST
  fill_in('Username', :with => 'admin')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end

def login_user(user)
  visit HOST
  fill_in('Username', :with => user.username)
  fill_in('Password', :with => user.password)
  click_button('Sign in')
end

def logout
  find('.t-user-settings').click
  click_link('Sign out')
end


def go_to_timeline
  find('.t-nav-timeline').click
end

