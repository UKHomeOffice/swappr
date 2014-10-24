require 'spec_helper'
require 'step_definitions/common_steps'

describe "the signin process", :type => :feature do
  before :each do
    login_user
  end

  it "signs me in" do
    expect(page).to have_selector('.sprite-user')
  end
end
