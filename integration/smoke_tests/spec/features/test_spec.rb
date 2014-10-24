require 'spec_helper'

describe "the signin process", :type => :feature do
  before :each do
  end

  it "signs me in" do
    visit 'http://localhost:8080/swap/1'
    expect(page).to have_selector('#username')
  end
end
